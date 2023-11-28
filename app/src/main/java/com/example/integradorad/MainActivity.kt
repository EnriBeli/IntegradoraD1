package com.example.integradorad

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.integradorad.ListarNotas.ListarNotas
import com.example.integradorad.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import android.view.View

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val fileResult = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth


        // Obtén una referencia a Firebase Authentication
        val auth = FirebaseAuth.getInstance()

// Obtén una referencia a Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val usuariosRef = database.getReference("Usuarios")

// Obtén el usuario actual
        val user = auth.currentUser

        if (user != null) {
            // Obten el ID del usuario actual
            val userId = user.uid

            // Obtén una referencia al nodo "Usuarios" y al nodo del usuario actual
            val usuarioActualRef = usuariosRef.child(userId)

            // Escucha los cambios en los datos del usuario actual
            usuarioActualRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val usuario = dataSnapshot.getValue(Usuario::class.java)

                        if (usuario != null) {

                            val nombresPrincipalTextView = findViewById<TextView>(R.id.NombresPrincipal)


                            // Actualiza los TextViews con los datos del usuario

                            nombresPrincipalTextView.text = "Nombre: " + usuario.nombre

                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar errores, si es necesario
                }
            })
        }





        /*updateUI()*/

        /*binding.updateProfileAppCompatButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()

            updateProfile(name)
        }

        binding.profileImageView.setOnClickListener {
            fileManager()
        }*/
        /*
        binding.deleteAccountTextView.setOnClickListener {
            val intent = Intent(this, DeleteAccountActivity::class.java)
            this.startActivity(intent)
        }

        binding.updatePasswordTextView.setOnClickListener {
            val intent = Intent(this, UpdatePasswordActivity  ::class.java)
            this.startActivity(intent)
        }*/


        binding.MiMedicoButton01.setOnClickListener {
            val intent = Intent(this, MiMedico::class.java)
            this.startActivity(intent)
        }

        binding.MisRecetasButton02.setOnClickListener {
            val intent = Intent(this, MisRecetasActivity::class.java)
            this.startActivity(intent)
        }

        binding.MisMedicamentosButton03.setOnClickListener {
            val intent = Intent(this, MisMedicamentosActivity::class.java)
            this.startActivity(intent)
        }

        binding.RegistrarCitaButton04.setOnClickListener {
            val intent = Intent(this, RegistrarCitaActivity::class.java)
            this.startActivity(intent)
        }

        binding.MisCitasButton05.setOnClickListener {
            val intent = Intent(this, ListarNotas::class.java)
            this.startActivity(intent)
        }

        binding.UbicacionButton06.setOnClickListener {
            val intent = Intent(this, ListaPersonasActivity::class.java)
            this.startActivity(intent)
        }

        binding.signOutImageView.setOnClickListener {
            showSignOutConfirmationDialog()
        }


    }

    private fun showSignOutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cerrar Sesión")
        builder.setMessage("¿Estás seguro de que quieres cerrar sesión?")

        builder.setPositiveButton("Sí") { dialog, which ->
            // Cerrar sesión
            signOut()
            finish()
            // Aquí puedes llamar a una función para enviar la notificación si es necesario
        }

        builder.setNegativeButton("No") { dialog, which ->
            // No hacer nada o realizar alguna acción adicional si el usuario elige no cerrar sesión
        }

        builder.show()
    }

    private fun signOut() {
        auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        this.startActivity(intent)
    }

    // Aquí puedes agregar una función para enviar la notificación si es necesario
    private fun sendLogoutNotification() {
        // Código para enviar la notificación
    }


    class Usuario {
        val nombre: String? = null
        val correo: String? = null
    }
    /*
        private  fun updateProfile (name : String) {

            val user = auth.currentUser

            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Se realizaron los cambios correctamente.",
                            Toast.LENGTH_SHORT).show()
                        updateUI()
                    }
                }
        }*/

    private fun fileManager() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, fileResult)
    }
    /*
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileResult) {
            if (resultCode == RESULT_OK && data != null) {
                val uri = data.data

                uri?.let { imageUpload(it) }

            }
        }
    }*/
    /*
    private fun imageUpload(mUri: Uri) {

        val user = auth.currentUser
        val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Users")
        val fileName: StorageReference = folder.child("img"+user!!.uid)

        fileName.putFile(mUri).addOnSuccessListener {
            fileName.downloadUrl.addOnSuccessListener { uri ->

                val profileUpdates = userProfileChangeRequest {
                    photoUri = Uri.parse(uri.toString())
                }

                user.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Se realizaron los cambios correctamente.",
                                Toast.LENGTH_SHORT).show()
                            updateUI()
                        }
                    }
            }
        }.addOnFailureListener {
            Log.i("TAG", "file upload error")
        }
    }
    */

    /*
    private  fun updateUI () {
        val user = auth.currentUser

        if (user != null){
            binding.emailTextView.text = user.email

            if(user.displayName != null){
                binding.nameTextView.text = user.displayName
                binding.nameEditText.setText(user.displayName)
            }

            Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .into(binding.profileImageView)
            /*Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .into(binding.bgProfileImageView)*/
        }

    }*/
    fun showNotification(view: View) {
        // Puedes cambiar NotificationActivity::class.java por tu clase y lógica específica.
        val intent = Intent(this, NotificationActivity::class.java)
        startActivity(intent)
    }


}


