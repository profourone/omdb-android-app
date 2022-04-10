package com.profourone.omdb.core.view

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T : ViewBinding> Fragment.viewBinding(factory: (View) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {
        private var binding: T? = null
        private var lifecycle: Lifecycle? = null

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
            binding ?: factory(requireView()).also {
                binding = it
                lifecycle = viewLifecycleOwner.lifecycle
                lifecycle?.addObserver(this)
            }

        override fun onDestroy(owner: LifecycleOwner) {
            lifecycle?.removeObserver(this)
            lifecycle = null
            binding = null
        }
    }

fun <T : ViewBinding> initBinding(binding: T, onBound: (T.() -> Unit)?): View {
    onBound?.invoke(binding)
    return binding.root
}
