package cz.teamnull.georgesenior.ui.template

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.teamnull.georgesenior.R

class TemplateFragment : Fragment() {

    companion object {
        fun newInstance() = TemplateFragment()
    }

    private lateinit var viewModel: TemplateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.template_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TemplateViewModel::class.java)
        // TODO: Use the ViewModel
    }

}