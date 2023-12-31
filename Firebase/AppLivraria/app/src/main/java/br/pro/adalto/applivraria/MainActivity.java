package br.pro.adalto.applivraria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spGeneros;
    private EditText etTitulo, etAutor;
    private Button btnSalvar;
    private String acao;
    private Livro livro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spGeneros = findViewById(R.id.spGenero);

        etTitulo = findViewById(R.id.etTitulo);
        etAutor = findViewById(R.id.etAutor);
        btnSalvar = findViewById(R.id.btnSalvar);
  //      btnSalvar.setEnabled(false);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        carregarGeneros();

        acao = getIntent().getExtras().getString("acao");
        if( acao.equals("editar")){
            livro = new Livro();
            livro.setId(  getIntent().getExtras().getString("idLivro") );
            etTitulo.setText( getIntent().getExtras().getString("titulo") );
            etAutor.setText( getIntent().getExtras().getString("autor") );
        }


    }

    private void salvar(){
        if( acao.equals("inserir")) {
            livro = new Livro();
        }

        String titulo = etTitulo.getText().toString();

        if( !titulo.isEmpty() && spGeneros.getSelectedItemPosition() > 0){
            livro.setTitulo( titulo );
            livro.setGenero( (Genero) spGeneros.getSelectedItem() );
            livro.setAutor( etAutor.getText().toString() );

//            LivroDAO.inserir( this, livro );

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference();

            if( acao.equals("inserir")) {
                reference.child("livros").push().setValue(livro);
            }else {
                reference.child("livros").child( livro.getId() ).setValue( livro );
            }



            finish();
        }


    }

    private void carregarGeneros(){
        Genero fake = new Genero(0, "Selecione o Gênero...");
        Genero acao = new Genero(1, "Ação");
        Genero romance = new Genero(2, "Romance");
        List<Genero> lista = new ArrayList<>(); // GeneroDAO.getGeneros(  this );
        lista.add(fake);
        lista.add(acao);
        lista.add(romance);


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista );
        spGeneros.setAdapter( adapter );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Cadastrar Gênero...");
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if( item.toString().equals("Cadastrar Gênero...")){
  //          GeneroDAO.inserir(MainActivity.this, "Romance");
  //          carregarGeneros();
            cadastrarGenero();
        }

        return super.onOptionsItemSelected(item);
    }

    private void cadastrarGenero(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("Cadastrar Gênero");
        alerta.setIcon(android.R.drawable.ic_input_add);

        EditText etNomeGenero = new EditText(this);
        etNomeGenero.setHint("Digite aqui o nome do gênero...");
    //    alerta.setMessage("Olá");
        alerta.setView( etNomeGenero );

        alerta.setNeutralButton("Cancelar", null);

        alerta.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nome = etNomeGenero.getText().toString();
                if( !nome.isEmpty() ){
       //             GeneroDAO.inserir(MainActivity.this, nome);
                    carregarGeneros();
                }
            }
        });
        alerta.show();

    }

}