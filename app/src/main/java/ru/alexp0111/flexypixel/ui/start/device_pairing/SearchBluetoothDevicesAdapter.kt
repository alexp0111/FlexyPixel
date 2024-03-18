package ru.alexp0111.flexypixel.ui.start.device_pairing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.alexp0111.flexypixel.databinding.ItemBluetoothDeviceBinding

class SearchBluetoothDevicesAdapter(
    private val onItemClicked: (BluetoothDeviceState) -> Unit,
) : RecyclerView.Adapter<SearchBluetoothDevicesAdapter.BluetoothDeviceViewHolder>() {

    private var list: MutableList<Pair<String, BluetoothDeviceState>> = mutableListOf()

    inner class BluetoothDeviceViewHolder(
        private val binding: ItemBluetoothDeviceBinding,
    ) : ViewHolder(binding.root) {
        fun bind(item: BluetoothDeviceState) {
            binding.apply {
                root.isEnabled = !item.isLoading
                txtName.text = item.name
                txtAddress.text = item.address

                ivIsConnected.isVisible = item.isConnected
                ivLoading.isVisible = item.isLoading
            }

            binding.root.setOnClickListener {
                onItemClicked.invoke(item)
            }
        }
    }

    fun updateList(incList: List<BluetoothDeviceState>) {
        val newListAsSet = incList.toSet()
        for (newItem in newListAsSet) {
            if (newItem.address !in list.map { it.first }) {
                list.add(Pair(newItem.address, newItem))
                notifyItemInserted(list.indexOfFirst { it.first == newItem.address } )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothDeviceViewHolder {
        val itemView = ItemBluetoothDeviceBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BluetoothDeviceViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BluetoothDeviceViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item.second)
    }

    fun setConnectedDevice(connectedDevice: BluetoothDeviceState?) {
        connectedDevice ?: return
        if (connectedDevice.address !in list.map { it.first }) return

        val index = list.indexOfFirst { it.first == connectedDevice.address }
        list[index] = Pair(connectedDevice.address, connectedDevice)
        notifyItemChanged(index)
    }
}