package com.appdev.kisanmitra.ui.disease

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.appdev.kisanmitra.databinding.FragmentDiseaseBinding
import com.appdev.kisanmitra.ml.PlantDiseaseClassifier
import com.appdev.kisanmitra.data.model.DiseaseTreatment.getTreatment

class DiseaseFragment : Fragment() {

    private lateinit var binding: FragmentDiseaseBinding
    private lateinit var classifier: PlantDiseaseClassifier

    private var selectedBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDiseaseBinding.inflate(inflater, container, false)

        classifier = PlantDiseaseClassifier(requireContext())

        binding.btnCamera.setOnClickListener {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,1)
        }

        binding.btnGallery.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,2)
        }

//        binding.btnAnalyze.setOnClickListener {
//
//            selectedBitmap?.let {
//
//                val result = classifier.predict(it)
//
//                val index = result.indices.maxByOrNull { result[it] } ?: -1
//
//                val disease = DiseaseLabels.labels[index]
//
//                val confidence = result[index] * 100
//
//                val treatment = DiseaseTreatment.getTreatment(disease)
//
//                val intent = Intent(requireContext(), DiseaseResultActivity::class.java)
//
//                intent.putExtra("disease",disease)
//                intent.putExtra("confidence",confidence)
//                intent.putExtra("treatment",treatment)
//
//                startActivity(intent)
//            }
//        }
        binding.resultCard.visibility = View.GONE

        binding.btnAnalyze.setOnClickListener {

            selectedBitmap?.let {

                val result = classifier.classify(it)

                binding.tvDisease.text = "Disease: ${result.first}"
                binding.tvConfidence.text = "Confidence: ${result.second}%"
                binding.tvTreatment.text = getTreatment(result.first)

                binding.resultCard.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode:Int,resultCode:Int,data:Intent?){
        super.onActivityResult(requestCode,resultCode,data)

        if(resultCode==Activity.RESULT_OK){

            if(requestCode==1){
                val bitmap=data?.extras?.get("data") as Bitmap
                selectedBitmap=bitmap
                binding.imagePreview.setImageBitmap(bitmap)
            }

            if(requestCode==2){
                val uri=data?.data
                val bitmap=MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,uri)
                selectedBitmap=bitmap
                binding.imagePreview.setImageBitmap(bitmap)
            }
        }
    }
}