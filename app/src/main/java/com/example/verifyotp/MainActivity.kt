package com.example.verifyotp



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var etphonenumber:EditText
    private lateinit var etotp:EditText
    private lateinit var btngetotp:Button
    private lateinit var btnenterotp:Button
    private lateinit var mAuth:FirebaseAuth
    var verificationId:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etphonenumber=findViewById(R.id.etPhoneNumber)
        etotp=findViewById(R.id.etotp)
        btngetotp=findViewById(R.id.btngetotp)
        btnenterotp=findViewById(R.id.btngenterotp)
        mAuth= Firebase.auth
        btngetotp.setOnClickListener {

            val number="+91${etphonenumber.text}"
            sendOtp(number)
        }
        btnenterotp.setOnClickListener {
            val otp=etotp.text.toString()
            verifyOtp(otp)
        }
    }
    fun verifyOtp(otp:String){
        val credentials=PhoneAuthProvider.getCredential(verificationId,otp)
        // verify otp with above credential
        signwithCredential(credentials)

    }
    fun signwithCredential(credential: PhoneAuthCredential){
        mAuth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this, "login sucessfully", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "some error occured", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun sendOtp( number:String){
        val options=PhoneAuthOptions
            .newBuilder(mAuth)
            .setActivity(this)
            .setPhoneNumber(number)
            .setCallbacks(verificationCallBack)
            .setTimeout(60L, TimeUnit.SECONDS)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    val verificationCallBack:OnVerificationStateChangedCallbacks=
        object :OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }

            override fun onCodeSent(s: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, p1)
                verificationId=s;

            }

        }
}