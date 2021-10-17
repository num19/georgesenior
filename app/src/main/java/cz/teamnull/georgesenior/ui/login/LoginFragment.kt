package cz.teamnull.georgesenior.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.children
import cz.teamnull.georgesenior.*
import cz.teamnull.georgesenior.databinding.LoginFragmentBinding
import cz.teamnull.georgesenior.utils.gotoAndFinish
import cz.teamnull.georgesenior.utils.pop
import cz.teamnull.georgesenior.utils.showMessage

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.login.children.forEach { v ->
            if (v is Button && v.text.length == 1) {
                v.setOnClickListener { onClick(v.text.toString()) }
            }
        }
        binding.buttonDelete.setOnClickListener {
            viewModel.pin = viewModel.pin.pop()
            updatePin()
        }
        binding.buttonUnknownPin.setOnClickListener {
            Toast.makeText(activity, "TODO", Toast.LENGTH_SHORT).show()
        }
        activity.gotoAndFinish(AccountActivity::class.java) // TODO: off
    }

    private val onClick: ((String) -> Unit) = {
        if (viewModel.pin.length < 4) {
            viewModel.pin += it
            updatePin()
            if (viewModel.check()) {
                activity.gotoAndFinish(AccountActivity::class.java)
            } else if (viewModel.pin.length == 4){
                viewModel.pin = ""
                updatePin()
                activity.showMessage("Nesprávný PIN", 5)
            }
        }
    }

    private fun updatePin() {
        binding.textViewPin.text = "*".repeat(viewModel.pin.length)
    }


}