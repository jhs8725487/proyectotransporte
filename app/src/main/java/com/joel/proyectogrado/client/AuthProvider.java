package com.joel.proyectogrado.client;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthProvider {
    FirebaseAuth mAuth;
    public AuthProvider(){
        mAuth=FirebaseAuth.getInstance();
    }
    public Task<AuthResult> Register(String email, String password){
        return mAuth.createUserWithEmailAndPassword(email, password);
    }
    public Task<AuthResult> Login(String email, String password){
        return mAuth.signInWithEmailAndPassword(email,password);
    }
    public void Logout(){
        mAuth.signOut();
    }
    public String getId(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public void existSession(){
        boolean exist=false;
        if (mAuth.getCurrentUser()!=null){
            exist=true;
        }
    }
}
