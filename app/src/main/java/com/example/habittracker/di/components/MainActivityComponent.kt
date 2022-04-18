package com.example.habittracker.di.components

import com.example.habittracker.di.annotations.MainActivityScope
import com.example.habittracker.presentation.ui.BottomSheetFragment
import com.example.habittracker.presentation.ui.HabitItemFragment
import com.example.habittracker.presentation.ui.HabitListFragment
import com.example.habittracker.presentation.ui.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent

@MainActivityScope
@Subcomponent
interface MainActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: MainActivity): MainActivityComponent
    }

    fun inject(bottomSheetFragment: BottomSheetFragment)

    fun inject(habitListFragment: HabitListFragment)

    fun inject(habitItemFragment: HabitItemFragment)

    fun inject(mainActivity: MainActivity)

}