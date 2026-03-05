package com.appdev.kisanmitra.data.model

object DiseaseTreatment {

    fun getTreatment(disease: String): String {

        return when {

            disease.contains("Early Blight") ->
                "Remove infected leaves and apply fungicide like Mancozeb."

            disease.contains("Late Blight") ->
                "Use copper fungicide and avoid excessive irrigation."

            disease.contains("Rust") ->
                "Apply sulfur spray and remove infected parts."

            disease.contains("Mosaic Virus") ->
                "Remove infected plants and control insect vectors."

            disease.contains("healthy") ->
                "Plant is healthy. Maintain proper irrigation and fertilization."

            else ->
                "Consult agricultural expert and monitor plant regularly."
        }
    }
}