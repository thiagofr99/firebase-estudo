package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth usuario = FirebaseAuth.getInstance();
    private ImageView imageFoto, imageDownload;
    private Button buttonUpload, buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference usuarios = referencia.child("usuario");

        buttonUpload = findViewById(R.id.buttonUpload);
        imageFoto = findViewById(R.id.imageView);
        buttonDelete = findViewById(R.id.buttonDelete);
        imageDownload = findViewById(R.id.imageDownload);

//Define nós para o storege
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference imagens = storageReference.child("imagens");
        final StorageReference imagemDownload = imagens.child("3244e18a-3be1-4caf-b921-8b7b92ee2982.jpeg");




        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Configura para imagem ser salva em memoria
                imageFoto.setDrawingCacheEnabled(true);
                imageFoto.buildDrawingCache();

                //Recupera bitmap da imagem (image a ser carregada)
                Bitmap bitmap = imageFoto.getDrawingCache();

                //Comprimo bitmap para um formato png/jpg
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);

                //converter o baos para pixel brutos em uma matriz de bytes
                //(dados da imagem)
                byte[] dadosImagem = baos.toByteArray();


                //NomeArquivo
                final String nomeArquivo = UUID.randomUUID().toString();
                final StorageReference imagemRef = imagens.child(nomeArquivo + ".jpeg");

                //Retorna objeto que irá controlar o upload
                UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

                uploadTask.addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("LoginUser", "Falha no upload da imagem");
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri url = task.getResult();
                                Log.i("LoginUser", "Sucesso ao fazer upload da imagem: url " + url);
                            }
                        });

                    }
                });


            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Deletar arquivo
                final StorageReference imagemDelete = imagens.child("ea3ba2cd-a97f-49af-a4bd-0efac4f04d38.jpeg");
                imagemDelete.delete().addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("LoginUser", "Erro ao deletar imagem");
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("LoginUser", "Sucesso ao deletar imagem");

                    }
                });

            }
        });

        imageFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(MainActivity.this).using(new FirebaseImageLoader()).load(imagemDownload).into(imageDownload);

            }
        });


        /*
        //Filtros
        //DatabaseReference usuarioPesquisa = usuarios.child("-MYDS1LmeFVDzTdHtKpv");
        //Query usuarioPesquisa = usuarios.orderByChild("nome").equalTo("Jamilton");
        //Query usuarioPesquisa = usuarios.orderByKey().limitToFirst(2);
        //Query usuarioPesquisa = usuarios.orderByKey().limitToLast(2);
        // Para buscas mais abranjentes de texto utilizar concatenar de + "\uf8ff" no endAt

        Query usuarioPesquisa = usuarios.orderByChild("idade").startAt(10).endAt(35);


        usuarioPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Importando do BD para uma classe
                Usuario dadosUsuario = snapshot.getValue(Usuario.class);
                Log.i("Dados usuario", "nome: "+ dadosUsuario.getNome()+"  sobrenome: "+dadosUsuario.getSobrenome());


                Log.i("Dados usuario", snapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

*/






/*

        //Metodo push (Gera indentificador unico)
        Usuario usuario1 = new Usuario("Jamilton","Giovanni",33);
        usuarios.push().setValue(usuario1);




        //Deslogar Usuario
        usuario.signOut();

        //Cadastrando usuario
        usuario.createUserWithEmailAndPassword("linkinht@gmail.com","14151617")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful()){
                            Log.i("CreateUser","Sucesso ao cadastrar usuario!");
                        } else {
                            Log.i("CreateUser","Erro ao cadastrar usuario!");
                        }

                    }
                });






        //Busca dados no bd
        usuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("FIREBASE", snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       Usuario usuario = new Usuario("Diego", "Furtado", 22);

        referencia.child("usuario").child("002").setValue(usuario);

        Produtos produtos = new Produtos("Pocophone F1","Xiaomi",2099.00);
        referencia.child("produtos").child("001").setValue(produtos);

        */

    }

    @Override
    protected void onStop() {
        super.onStop();
        usuario.signOut();
        Log.i("LoginUser", "Usuario deslogado");

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Verifica usuario logado

        if (usuario.getCurrentUser() != null) {
            Log.i("LoginUser", "Usuario logado");
        } else {
            Log.i("LoginUser", "Usuario deslogado");
            //Logar usuario
            usuario.signInWithEmailAndPassword("linkinht@gmail.com", "14151617").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i("LoginUser", "Sucesso ao Logar usuario!");
                    } else {
                        Log.i("LoginUser", "Erro ao logar usuario!");
                    }
                }
            });
        }


    }
}