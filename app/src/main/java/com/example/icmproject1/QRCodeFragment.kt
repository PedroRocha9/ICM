package com.example.icmproject1

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QRCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QRCodeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val options = BarcodeScannerOptions.Builder(
    ).setBarcodeFormats(
        Barcode.FORMAT_QR_CODE,
        Barcode.FORMAT_AZTEC
    ).build()

    private val REQUEST_IMAGE_CAPTURE = 1
    private var imageBitmap: Bitmap?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_q_r_code, container, false)

        val captureImage = viewRoot.findViewById<Button>(R.id.capture_image)
        val textView = viewRoot.findViewById<TextView>(R.id.text_view)
        val detectScan = viewRoot.findViewById<Button>(R.id.detect_scan)

        captureImage.setOnClickListener {
            takeImage()

            textView.text=""
        }

        detectScan.setOnClickListener {
            detectImage()
        }


        // Inflate the layout for this fragment
        return viewRoot
    }

    private fun detectImage() {
       if (imageBitmap != null) {
           val image = InputImage.fromBitmap(imageBitmap!!, 0)
           val scanner = BarcodeScanning.getClient(options)

           scanner.process(image)
               .addOnSuccessListener { barcodes ->
                   if (barcodes.toString() == "[]") {
                       Toast.makeText(requireContext(), "Nothing to scan", Toast.LENGTH_SHORT).show()
                   }

                   for (barcode in barcodes) {
                       val valueType = barcode.valueType

                       when(valueType) {
                           Barcode.TYPE_TEXT -> {
                               val text = barcode.displayValue
                               val textView = view?.findViewById<TextView>(R.id.text_view)
                               textView?.text = text
                           }
                           else -> {
                               Toast.makeText(requireContext(), "Nothing to scan", Toast.LENGTH_SHORT).show()
                           }
                       }
                   }
               }
       }

        else {
            Toast.makeText(requireContext(), "Please select photo", Toast.LENGTH_SHORT).show()
       }
    }

    private fun takeImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the captured image as a bitmap
            val imageBitmap = data?.extras?.get("data") as Bitmap?

            if (imageBitmap != null) {
                // Set the bitmap to the ImageView
                val imageView = view?.findViewById<ImageView>(R.id.capture_image)
                imageView?.setImageBitmap(imageBitmap)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QRCodeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QRCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}