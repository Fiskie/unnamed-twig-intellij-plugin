package com.fisk.twig

import com.intellij.CommonBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey
import java.lang.ref.Reference
import java.lang.ref.SoftReference
import java.util.*

object TwigBundle {
    fun message(@PropertyKey(resourceBundle = PATH_TO_BUNDLE) key: String, vararg params: Any): String {
        return CommonBundle.message(bundle, key, *params)
    }

    private var ourBundle: Reference<ResourceBundle>? = null
    const @NonNls private val PATH_TO_BUNDLE = "messages.TwigBundle"

    private val bundle: ResourceBundle
        get() {
            var bundle = com.intellij.reference.SoftReference.dereference(ourBundle)
            if (bundle == null) {
                bundle = ResourceBundle.getBundle(PATH_TO_BUNDLE)
                ourBundle = SoftReference<ResourceBundle>(bundle)
            }
            return bundle!!
        }
}
