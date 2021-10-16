package cz.teamnull.georgesenior.ui.account

import android.graphics.PorterDuff
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import androidx.core.content.ContextCompat
import cz.teamnull.georgesenior.R
import cz.teamnull.georgesenior.databinding.AccountFragmentBinding

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
        binding.buttonListen.setOnClickListener {
            viewModel.isListening = !viewModel.isListening
            updateListeningButton()
        }
        micAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.mic)
    }

    private fun updateListeningButton() = binding.apply {
        if (viewModel.isListening) {
            /*imageListening.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                ), PorterDuff.Mode.SRC_IN
            )*/
            imageListening.startAnimation(micAnim)
            textViewListening.text = "Poslouchám"
        } else {
            /*imageListening.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.george_blue
                ), PorterDuff.Mode.SRC_IN
            )*/
            imageListening.clearAnimation()
            textViewListening.text = "Říct to"
        }

    }

}