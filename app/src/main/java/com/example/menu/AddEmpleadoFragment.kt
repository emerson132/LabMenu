package com.example.menu

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.ETC1.decodeImage
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.example.menu.entity.Empleado
import com.example.menu.repository.EmpleadoRepository
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val PICK_IMAGE = 100
/**
 * A simple [Fragment] subclass.
 * Use the [AddEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var empleado: Empleado? = null
    private var param1: String? = null
    lateinit var img_avatar : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_add_empleado, container, false)

        val agregar = view.findViewById<Button>(R.id.button3)

        img_avatar = view.findViewById(R.id.avatar)


        agregar.setOnClickListener{
            val builder = AlertDialog.Builder(context)

            builder.setMessage("¿Desea agregar el registro?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->
                    var idEmpleado: Int = EmpleadoRepository.instance.datos().size + 1

                    var emple =  Empleado(
                    idEmpleado,
                     view.findViewById<EditText>(R.id.editTextTextPersonName).text.toString(),
                     view.findViewById<EditText>(R.id.editTextTextPersonName2).text.toString(),
                     view.findViewById<EditText>(R.id.editTextTextPersonName3).text.toString(),
                     view.findViewById<EditText>(R.id.editTextTextPersonName4).text.toString(),
                    encodeImage(img_avatar.drawable.toBitmap())!!
                    )
                    EmpleadoRepository.instance.save(emple)
                    val fragmento = CamaraFragment.newInstance("Camara")
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                    activity?.setTitle("Empleados")
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id ->
                    // logica del no
                }
            val alert = builder.create()
            alert.show()


        }
        img_avatar.setOnClickListener {
            var galeria = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(galeria, PICK_IMAGE)
        }

        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){

            var imageUri = data?.data

            Picasso.get()
                .load(imageUri)
                .resize(120,120)
                .centerCrop()
                .into(img_avatar)
        }
    }
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT).replace("\n","")
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment AddEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            AddEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}