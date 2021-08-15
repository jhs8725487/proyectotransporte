package com.joel.proyectogrado.providers;
import com.joel.proyectogrado.models.Client;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ClientProvider {

    DatabaseReference mDatabase;
    public ClientProvider(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Clients");
    }
    public Task<Void> create(Client client){
        Map<String, Object> map=new HashMap<>();
        map.put("name", client.getNombre());
        map.put("email",client.getCorreo());
        return mDatabase.child(client.getId()).setValue(map);
    }
}
