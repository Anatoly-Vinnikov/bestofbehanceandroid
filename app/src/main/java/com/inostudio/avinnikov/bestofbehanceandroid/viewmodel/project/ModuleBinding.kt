package com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.project

import android.databinding.BaseObservable
import android.databinding.Bindable

class ModuleBinding(
        @Bindable val max1920: String = "",
        @Bindable val moduleText: String = "",
        @Bindable val webView: String = ""
) : BaseObservable()