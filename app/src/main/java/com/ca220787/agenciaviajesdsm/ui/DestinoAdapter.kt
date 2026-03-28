package com.ca220787.agenciaviajesdsm.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ca220787.agenciaviajesdsm.R
import com.ca220787.agenciaviajesdsm.data.Destino
import com.ca220787.agenciaviajesdsm.util.ImgUtil
import java.text.DecimalFormat

class DestinoAdapter(
    private val lisDest: MutableList<Destino>,
    private val alPuls: (Destino) -> Unit
) : RecyclerView.Adapter<DestinoAdapter.DestinoVH>() {

    private val decFor = DecimalFormat("$0.00")

    inner class DestinoVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDest: ImageView = itemView.findViewById(R.id.imgDest)
        val tvNomb: TextView = itemView.findViewById(R.id.tvNomb)
        val tvPais: TextView = itemView.findViewById(R.id.tvPais)
        val tvPrec: TextView = itemView.findViewById(R.id.tvPrec)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinoVH {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_destino, parent, false)
        return DestinoVH(vista)
    }

    override fun onBindViewHolder(holder: DestinoVH, position: Int) {
        val dest = lisDest[position]

        holder.tvNomb.text = dest.nombDest
        holder.tvPais.text = dest.paisDest
        holder.tvPrec.text = decFor.format(dest.precDest)

        val bitMap = ImgUtil.base64ABitmap(dest.imagBase)
        if (bitMap != null) {
            holder.imgDest.setImageBitmap(bitMap)
        } else {
            holder.imgDest.setImageResource(R.mipmap.ic_launcher)
        }

        holder.itemView.setOnClickListener {
            alPuls(dest)
        }
    }

    override fun getItemCount(): Int = lisDest.size

    fun actualizarDatos(nueLis: List<Destino>) {
        lisDest.clear()
        lisDest.addAll(nueLis)
        notifyDataSetChanged()
    }
}