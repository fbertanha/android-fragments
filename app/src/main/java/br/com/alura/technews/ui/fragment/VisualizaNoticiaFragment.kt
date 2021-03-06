package br.com.alura.technews.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.activity.NOTICIA_ID_CHAVE
import br.com.alura.technews.ui.fragment.extensios.mostraErro
import br.com.alura.technews.ui.viewmodel.VisualizaNoticiaViewModel
import kotlinx.android.synthetic.main.fragment_visualiza_noticia.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by felipebertanha on 28/May/2020
 */

private const val TITULO_APPBAR = "Notícia"

private const val NOTICIA_NAO_ENCONTRADA = "Notícia não encontrada"
private const val MENSAGEM_FALHA_REMOCAO = "Não foi possível remover notícia"

class VisualizaNoticiaFragment : BaseFragment() {

    var onEditaNoticia: (noticia : Noticia) -> Unit = {}
    var onFechar: () -> Unit = {}

    private val noticiaId: Long by lazy {
        arguments?.getLong(NOTICIA_ID_CHAVE)
            ?: throw IllegalArgumentException("noticiaId cannot be null")
    }
    private val viewModel: VisualizaNoticiaViewModel by viewModel { parametersOf(noticiaId) }

    companion object {
        val TAG: String = VisualizaNoticiaFragment::class.java.name

        fun newInstance(noticiaId: Long): VisualizaNoticiaFragment {
            val visualizaNoticiaFragment = VisualizaNoticiaFragment()

            val bundle = Bundle()
            bundle.putLong(NOTICIA_ID_CHAVE, noticiaId)
            visualizaNoticiaFragment.arguments = bundle

            return visualizaNoticiaFragment
        }

        fun newInstance(bundle: Bundle?): VisualizaNoticiaFragment {
            val visualizaNoticiaFragment = VisualizaNoticiaFragment()
            visualizaNoticiaFragment.arguments = bundle
            return visualizaNoticiaFragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verificaIdDaNoticia()
        buscaNoticiaSelecionada()
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = TITULO_APPBAR
    }

    override fun getLayout(): Int {
        return R.layout.fragment_visualiza_noticia
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.visualiza_noticia_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.e("", "aaa")

        when (item?.itemId) {
            R.id.visualiza_noticia_menu_edita -> {
                Log.e("", "bbb ${viewModel.noticiaEncontrada.value}")
                viewModel.noticiaEncontrada.value?.let { onEditaNoticia(it) }
            }
            R.id.visualiza_noticia_menu_remove -> remove()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun buscaNoticiaSelecionada() {
        viewModel.noticiaEncontrada.observe(this, Observer { noticiaEncontrada ->
            noticiaEncontrada?.let {
                preencheCampos(it)
            }
        })
    }

    private fun verificaIdDaNoticia() {
        if (noticiaId == 0L) {
            mostraErro(NOTICIA_NAO_ENCONTRADA)
            onFechar()
        }
    }

    private fun preencheCampos(noticia: Noticia) {
        fragment_visualiza_noticia_titulo.text = noticia.titulo
        fragment_visualiza_noticia_texto.text = noticia.texto
    }

    private fun remove() {
        viewModel.remove().observe(this, Observer {
            if (it.erro == null) {
                onFechar()
            } else {
                mostraErro(MENSAGEM_FALHA_REMOCAO)
            }
        })
    }
}