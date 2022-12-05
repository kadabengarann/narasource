package id.co.mka.narasource.narasumber.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import id.co.mka.narasource.di.NarasumberModuleDependencies
import id.co.mka.narasource.narasumber.presentation.activity.ActivityFragment

@Component(dependencies = [NarasumberModuleDependencies::class])
interface NarasumberComponent {
    fun inject(activityFragment: ActivityFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(narasumberModuleDependencies: NarasumberModuleDependencies): Builder
        fun build(): NarasumberComponent
    }
}
