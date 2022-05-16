package com.example.menu

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
private const val ARG_PARAM2 = "param2"
private const val PICK_IMAGE = 100
/**
 * A simple [Fragment] subclass.
 * Use the [EditEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var empleado: Empleado? = null
    lateinit var img_avatar : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            empleado = it.get(ARG_PARAM1) as Empleado?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_edit_empleado, container, false)

        val textN = view.findViewById<EditText>(R.id.editTextTextPersonName2)
        textN.setText(empleado?.nombre)

        val textP = view.findViewById<EditText>(R.id.editTextTextPersonName3)
        textP.setText(empleado?.puesto)

        val textD = view.findViewById<EditText>(R.id.editTextTextPersonName4)
        textD.setText(empleado?.departamento)

        val textI = view.findViewById<EditText>(R.id.editTextTextPersonName)
        textI.setText(empleado?.identificacion)



        val modificar = view.findViewById<Button>(R.id.button)
        val eliminar = view.findViewById<Button>(R.id.button2)

        img_avatar = view.findViewById(R.id.avatar)

        if(empleado?.avatar != ""){
            img_avatar.setImageBitmap(empleado?.avatar?.let { decodeImage(it) })
        }


        modificar.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setMessage("¿Desea modificar el registro?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->
                    empleado?.identificacion = textI.text.toString()
                    empleado?.nombre = textN.text.toString()
                    empleado?.puesto = textP.text.toString()
                    empleado?.departamento = textD.text.toString()
                    empleado?.avatar = encodeImage(img_avatar.drawable.toBitmap())!!


                    empleado?.let { itl -> EmpleadoRepository.instance.edit(itl) }
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
        eliminar.setOnClickListener {

            val builder = AlertDialog.Builder(context)

            builder.setMessage("¿Desea Eliminar el registro?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->
                    empleado?.let { it1 -> EmpleadoRepository.instance.delete(it1) }
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
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){

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

    private fun decodeImage (b64 : String): Bitmap {
        val imageBytes = Base64.decode(b64, 0)
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(empleado: Empleado) =
            EditEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, empleado)
                }
            }
    }
}