package com.appdev.kisanmitra.ui.disease

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appdev.kisanmitra.databinding.ActivityDiseaseResultBinding

class DiseaseResultActivity:AppCompatActivity(){

    private lateinit var binding:ActivityDiseaseResultBinding

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)

        binding=ActivityDiseaseResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val disease=intent.getStringExtra("disease")
        val confidence=intent.getFloatExtra("confidence",0f)
        val treatment=intent.getStringExtra("treatment")

        binding.tvDisease.text="Disease: $disease"
        binding.tvConfidence.text="Confidence: %.2f%%".format(confidence)
        binding.tvTreatment.text="Treatment:\n$treatment"
    }
}