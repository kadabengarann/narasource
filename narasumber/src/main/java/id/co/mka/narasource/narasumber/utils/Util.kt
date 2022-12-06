package id.co.mka.narasource.narasumber.utils

import dagger.hilt.android.EntryPointAccessors
import id.co.mka.narasource.di.NarasumberModuleDependencies
import id.co.mka.narasource.narasumber.di.DaggerNarasumberComponent
import id.co.mka.narasource.narasumber.presentation.activity.ActivityDetailFragment
import id.co.mka.narasource.narasumber.presentation.activity.ActivityFragment

internal fun ActivityFragment.inject() {
    val component = DaggerNarasumberComponent.builder()
        .context(requireContext())
        .appDependencies(
            EntryPointAccessors.fromApplication(
                requireContext(),
                NarasumberModuleDependencies::class.java
            )
        )
        .build()
    component.inject(this)
}

internal fun ActivityDetailFragment.inject() {
    val component = DaggerNarasumberComponent.builder()
        .context(requireContext())
        .appDependencies(
            EntryPointAccessors.fromApplication(
                requireContext(),
                NarasumberModuleDependencies::class.java
            )
        )
        .build()
    component.injectDetail(this)
}
