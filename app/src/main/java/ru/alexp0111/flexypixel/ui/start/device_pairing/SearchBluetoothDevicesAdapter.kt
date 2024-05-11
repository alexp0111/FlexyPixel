package ru.alexp0111.flexypixel.ui.start.device_pairing

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.alexp0111.flexypixel.databinding.ItemBluetoothDeviceBinding
import soup.neumorphism.NeumorphCardView
import soup.neumorphism.NeumorphShapeAppearanceModel
import soup.neumorphism.NeumorphShapeDrawable

class SearchBluetoothDevicesAdapter(
    private val onItemClicked: (BluetoothDeviceState) -> Unit,
) : RecyclerView.Adapter<SearchBluetoothDevicesAdapter.BluetoothDeviceViewHolder>() {

    var list: MutableList<Pair<String, BluetoothDeviceState>> = mutableListOf()

    inner class BluetoothDeviceViewHolder(
        private val binding: ItemBluetoothDeviceBinding,
    ) : ViewHolder(binding.root) {
        fun bind(item: BluetoothDeviceState) {
            binding.apply {
                root.isEnabled = !item.isLoading
                deviceName.apply {
                    text = item.name.ifEmpty { item.address }
                    isSelected = true
                }

                if (item.isConnected) {
                    connectionIndicator.apply {
                        setShadowElevation(600f)
                        setStrokeWidth(2f)
                    }
                } else {
                    connectionIndicator.apply {

                        setShadowElevation(16.5f)
                        setStrokeWidth(0f)
                    }
                }
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
                notifyItemInserted(list.indexOfFirst { it.first == newItem.address })
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