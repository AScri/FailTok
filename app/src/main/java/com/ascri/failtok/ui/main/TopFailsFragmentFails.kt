package com.ascri.failtok.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ascri.failtok.ui.viewModels.FailsViewModel

class TopFailsFragmentFails : FailsViewerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        type = FailsViewModel.FailsType.TOP_TYPE
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}