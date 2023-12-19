package co.geisyanne.meuapp.drawTeams.register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.FragmentPlayerRegisterBinding


class PlayerRegisterFragment : Fragment(R.layout.fragment_player_register) {


    private var binding: FragmentPlayerRegisterBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("teste", "inicio")

        binding = FragmentPlayerRegisterBinding.bind(view)

        binding?.playerRegisterEditName?.addTextChangedListener(watcher)

        val positions = resources.getStringArray(R.array.positions)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, positions)

        Log.e("teste", "meio")

        binding?.playerRegisterTxtDropdownPositions?.apply {
            setAdapter(arrayAdapter)
            setText(arrayAdapter.getItem(0), false)
            setOnClickListener {
                showDropDown()
            }
        }

        val btnSave = binding?.playerRegisterBtnSave
        btnSave?.setOnClickListener {
            btnSave.showProgress(true)

            Log.e("teste", "clique")

            Handler(Looper.getMainLooper()).postDelayed({
                btnSave.showProgress(false)
            }, 2000)
        }

    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding?.playerRegisterBtnSave?.isEnabled = s.toString().isNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }


}