package com.example.sistemadefacturacion_pruni.Cliente.Nav_Fragments_Cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sistemadefacturacion_pruni.Cliente.Bottom_Nav_Fragments_Cliente.FragmentMisOrdenesC
import com.example.sistemadefacturacion_pruni.Cliente.Bottom_Nav_Fragments_Cliente.FragmentTiendaClienteC
import com.example.sistemadefacturacion_pruni.R
import com.example.sistemadefacturacion_pruni.databinding.FragmentInicioClienteBinding


class FragmentInicioCliente : Fragment() {


private lateinit var binding: FragmentInicioClienteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioClienteBinding.inflate(inflater, container, false)
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.op_tienda_c -> {
                    replaceFragment(FragmentTiendaClienteC())

                }
                R.id.op_mis_ordenes_c -> {
                    replaceFragment(FragmentMisOrdenesC())
                }
            }
            true

            }
        replaceFragment(FragmentTiendaClienteC())
        binding.bottomNavigation.selectedItemId = R.id.op_tienda_c
        // Inflate the layout for this fragment
        return binding.root


    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment,fragment)
            .commit()
    }
}
