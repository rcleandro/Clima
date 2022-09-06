package br.com.leandro.clima.presentation.recyclerview.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.com.leandro.clima.data.Forecast
import br.com.leandro.clima.presentation.util.Helper.Companion.convertDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForecastAdapter : ListAdapter<DataItem, DataItemViewHolder>(ForecastCardDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataItemViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> DataItemViewHolder.HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> DataItemViewHolder.ForecastCardViewHolder.from(parent)
            else -> throw ClassCastException("ViewType desconhecido ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: DataItemViewHolder, position: Int) {
        when (holder) {
            is DataItemViewHolder.ForecastCardViewHolder -> {
                val item = getItem(position) as DataItem.ForecastCardItem
                holder.bind(item.forecast)
            }
            is DataItemViewHolder.HeaderViewHolder -> {
                val item = getItem(position) as DataItem.Header
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ForecastCardItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeadersAndSubmitList(list: List<Forecast>?) {
        adapterScope.launch {
            val listDataItem = list?.toListOfDataItem()
            withContext(Dispatchers.Main) {
                submitList(listDataItem)
            }
        }
    }

    class ForecastCardDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    private fun List<Forecast>.toListOfDataItem(): List<DataItem> {

        val grouping = this.groupBy { forecast ->
            convertDate(forecast.dt)
        }

        val listDataItem = mutableListOf<DataItem>()
        grouping.forEach { mapEntry ->
            listDataItem.add(DataItem.Header(mapEntry.key))
            listDataItem.addAll(
                mapEntry.value.map { forecast ->
                    DataItem.ForecastCardItem(forecast)
                }
            )
        }

        return listDataItem
    }
}