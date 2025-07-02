package com.example.sistemadefacturacion_pruni.Vendedor.Nav_Fragment_Vendedor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sistemadefacturacion_pruni.R
import com.example.sistemadefacturacion_pruni.SplashScreenActivity

class FragmentMiTiendaV : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mi_tienda_v, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lanzar el SplashScreen cuando se abra este Fragment
        val intent = Intent(requireContext(), SplashScreenActivity::class.java)
        startActivity(intent)

    }
}
