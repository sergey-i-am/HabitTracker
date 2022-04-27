package com.example.habittracker.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.habittracker.R
import com.example.habittracker.app.applicationComponent
import com.example.habittracker.databinding.ActivityMainBinding
import com.example.habittracker.di.components.MainActivityComponent
import com.example.habittracker.domain.models.CloudError
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.HabitType
import com.example.habittracker.presentation.models.AddHabitDoneResult
import com.example.habittracker.presentation.view_models.HabitListViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private var currentFragment: Fragment? = null

    lateinit var mainActivityComponent: MainActivityComponent

    @Inject
    lateinit var viewModel: HabitListViewModel

    private val fragmentListener =
        object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fragmentManager: FragmentManager,
                fragment: Fragment,
                view: View,
                savedInstanceState: Bundle?
            ) {
                super.onFragmentViewCreated(fragmentManager, fragment, view, savedInstanceState)
                if (fragment is NavHostFragment) return
                currentFragment = fragment
                updateTitle()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
        setupMainActivityComponent()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupNavigation()
        setupViewModel()
        setupHeaderImage()

    }

    private fun setupHeaderImage() {
        val header = binding.navigationView.getHeaderView(DEFAULT_HEADER)
        val avatar = header.findViewById<ImageView>(R.id.avatar)
            ?: throw RuntimeException("Header image view is null")
        Glide.with(this)
            .load(IMAGE_URL)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.no_image_available)
            .circleCrop()
            .into(avatar)
    }

    private fun setupMainActivityComponent() {
        mainActivityComponent = applicationComponent
            .mainActivityComponentFactory()
            .create(this)
        mainActivityComponent.inject(this)
    }

    private fun makeToast(text: String, duration: Int) {
        Toast.makeText(this, text, duration).show()
    }

    private fun makeCloudErrorToast(cloudError: CloudError) {
        val codeString =
            if (cloudError.code != 0) ", code: ${cloudError.code}"
            else EMPTY_CODE_STRING
        makeToast("${cloudError.message}$codeString", Toast.LENGTH_LONG)
    }

    private fun setupViewModel() {
        viewModel.errorCloud.observe(this) {
            if (it is Either.Failure) {
                makeCloudErrorToast(it.error)
            }
        }

        viewModel.showResultToast.observe(this) {
            it.transferIfNotHandled()?.let { result ->
                when (result) {
                    is Either.Success -> makeToast(getString(result.result), Toast.LENGTH_SHORT)
                    is Either.Failure -> makeCloudErrorToast(result.error)
                }
            }
        }

        viewModel.showSnackbarHabitDone.observe(this) {
            it.transferIfNotHandled()?.let { result ->
                val habitType = result.habit.type
                val habitRecurrenceNumber = result.habit.recurrenceNumber
                val actualDoneListSize = result.habit.actualDoneListSize()
                val differenceDone = abs(actualDoneListSize - habitRecurrenceNumber)
                val differenceDoneTimes = resources.getQuantityString(
                    R.plurals.plurals_more_times,
                    differenceDone,
                    differenceDone
                )
                val snackbarText = when (habitType) {
                    HabitType.GOOD -> {
                        if (actualDoneListSize < habitRecurrenceNumber)
                            getString(R.string.worth_doing_more_times, differenceDoneTimes)
                        else getString(R.string.you_are_breathtaking)
                    }
                    HabitType.BAD -> {
                        if (actualDoneListSize < habitRecurrenceNumber)
                            getString(R.string.you_are_allowed_more_times, differenceDoneTimes)
                        else getString(R.string.stop_doing_it)
                    }
                }
                Snackbar.make(binding.drawerLayout, snackbarText, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) {
                        viewModel.deleteHabitDone(result.habitDone.id)
                    }
                    .addCallback(snackbarCallback(result))
                    .show()
            }
        }
    }

    private fun snackbarCallback(result: AddHabitDoneResult) =
        object : BaseTransientBottomBar.BaseCallback<Snackbar>() {

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    viewModel.addHabitDoneToCloud(result.habitDone)
                }
                viewModel.unblockHabitDoneButtons()
            }

            override fun onShown(sb: Snackbar?) {
                super.onShown(sb)
                viewModel.blockHabitDoneButtons()
            }
        }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navigationView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
    }

    private fun updateTitle() {
        val fragment = currentFragment
        if (fragment is HasTitle) {
            binding.toolbar.title = getString(fragment.getTitleResId())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        MenuCompat.setGroupDividerEnabled(menu, true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_upload -> viewModel.uploadAllHabitsFromDbToCloud()
            R.id.menu_download -> viewModel.downloadAllHabitsFromCloudToDb()
            R.id.menu_clear_db -> viewModel.deleteAllHabitsFromDb()
            R.id.menu_clear_cloud -> viewModel.deleteAllHabitsFromCloud()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val IMAGE_URL =
            "https://img.freepik.com/free-photo/no-problem-concept-bearded-man-makes-okay-gesture-has-everything-control-all-fine-gesture-wears-spectacles-jumper-poses-against-pink-wall-says-i-got-this-guarantees-something_273609-42817.jpg"
        private const val DEFAULT_HEADER = 0
        private const val EMPTY_CODE_STRING = ""
    }
}