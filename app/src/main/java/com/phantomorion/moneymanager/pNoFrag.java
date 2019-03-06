package com.phantomorion.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class pNoFrag extends Fragment {
    EditText edTxt;
    Button otpButton;
    String phoneNumber;
    Spinner pNoSpinner;
    private String OTP;
    FirebaseAuth mAuth;
    ProgressBar pBar;
    SharedPreferences spref;
    SharedPreferences.Editor edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_p_no, container, false);
        spref = view.getContext().getSharedPreferences("userName",Context.MODE_PRIVATE);
        edit=spref.edit();
        edTxt=view.findViewById(R.id.edTxt);
        otpButton=view.findViewById(R.id.otpButton);
        pNoSpinner=view.findViewById(R.id.pNoSpinner);

        pBar=view.findViewById(R.id.pBar);
        pNoSpinner.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,countryCodes.countryNames));
        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryCode=countryCodes.countryAreaCodes[pNoSpinner.getSelectedItemPosition()];
                String pNo=edTxt.getText().toString().trim();
                if(pNo.isEmpty()||pNo.length()<10)
                {
                    edTxt.setError("Please enter a valid phone number");
                    edTxt.requestFocus();
                    return;
                }
                phoneNumber="+"+countryCode+pNo;
       //         phoneImg.setVisibility(View.GONE);
                pNoSpinner.setVisibility(View.GONE);
                edTxt.setText("");
                edTxt.setHint("Enter OTP (valid only for 60 seconds)");
                otpButton.setText("Verify OTP");
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        TaskExecutors.MAIN_THREAD,
                        mCallbacks);
                otpButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code=edTxt.toString().trim();
                        if (code.isEmpty() || code.length() < 6) {
                         edTxt.setError("Please enter a valid OTP");
                         edTxt.requestFocus();
                         return;
                        }else {
                            pBar.setVisibility(View.VISIBLE);
                            verifyCode(code);
                        }
                    }
                });
            }
            private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    OTP=s;
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    String code=phoneAuthCredential.getSmsCode();
                    if(code!=null)
                    {
                        pBar.setVisibility(View.VISIBLE);
                        verifyCode(code);
                        edTxt.setText(code);
                    }
                }
                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            };
        });

        return view;
    }
private void verifyCode(String code)
{
    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(OTP,code);
    signInWithCredential(credential);
}

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth=FirebaseAuth.getInstance();
    mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful())
            {   pBar.setVisibility(View.GONE);
                Intent gSignIn = new Intent(getContext(), SingleFamilySignInActivity.class);
                edit.putString("userName",phoneNumber);
                edit.commit();
                gSignIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(gSignIn);

            }
            else
            {   pBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    });

    }


}
