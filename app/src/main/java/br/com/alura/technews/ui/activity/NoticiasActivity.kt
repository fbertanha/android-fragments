package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.activity.extensions.addFragment
import br.com.alura.technews.ui.activity.extensions.replaceFragment
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment

private const val TITULO_APPBAR = "Notícias"

class NoticiasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)
        title = TITULO_APPBAR

        addFragment(
            R.id.activity_noticias_container,
            ListaNoticiasFragment()
        )

    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)

        if (fragment is ListaNoticiasFragment) {
            fragment.onNoticiaSelecionada = {
                abreVisualizadorNoticia(it)
            }
            fragment.onNovaNoticiaClicado = {
                abreFormularioModoCriacao()
            }
        }

        if (fragment is VisualizaNoticiaFragment) {
            fragment.onFechar = {
                finish()
            }

            fragment.onRemoveNoticia = {
                finish()
            }

            fragment.onEditaNoticia = {
                noticiaSelecionada ->
                abreFormularioEdicao(noticiaSelecionada)
            }
        }
    }

    private fun abreFormularioEdicao(noticia: Noticia) {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, noticia.id)
        startActivity(intent)
    }

    private fun abreFormularioModoCriacao() {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        startActivity(intent)
    }

    private fun abreVisualizadorNoticia(noticia: Noticia) {

        replaceFragment(R.id.activity_noticias_container,
            VisualizaNoticiaFragment.newInstance(noticia.id))


    }

}