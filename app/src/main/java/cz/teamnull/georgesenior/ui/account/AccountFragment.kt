package cz.teamnull.georgesenior.ui.account

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cz.teamnull.georgesenior.InfoActivity
import cz.teamnull.georgesenior.R
import cz.teamnull.georgesenior.TemplateActivity
import cz.teamnull.georgesenior.databinding.AccountFragmentBinding
import cz.teamnull.georgesenior.utils.goto

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private lateinit var viewModel: AccountViewModel
    private lateinit var binding: AccountFragmentBinding
    private lateinit var micAnim: Animation

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = AccountFragmentBinding.bind(inflater.inflate(R.layout.account_fragment, container, false))
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        viewModel.balanceChanged = { activity?.runOnUiThread { updateBalance() } }
        updateBalance()
        binding.buttonListen.setOnClickListener {
            if (checkPermission() && !viewModel.isListening) {
                viewModel.listen()
            }
        }
        binding.cardSend.setOnClickListener { activity.goto(TemplateActivity::class.java) }
        binding.cardReceive.setOnClickListener { activity.goto(InfoActivity::class.java) }
        micAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.mic)
        viewModel.onPrepared = {
            updateListeningButton()
        }
        viewModel.onFinished = {
            updateListeningButton()
        }
        viewModel.init(requireActivity())

    }

    private fun checkPermission() = if(ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        false
    } else {
        true
    }

    private fun updateBalance() {
        binding.textViewMoney.text = String.format("% .2f Kč", AccountViewModel.balance)
    }

    private fun updateListeningButton() = binding.apply {
        if (viewModel.isListening) {
            imageListening.startAnimation(micAnim)
            textViewListening.text = "Mluvte"
        } else {
            imageListening.clearAnimation()
            textViewListening.text = "Říct něco"
        }

    }

}