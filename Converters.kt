package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.ContradictionItem
import org.json.JSONArray
import org.json.JSONObject

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach { array.put(it) }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<String>()
        try {
            val array = JSONArray(value)
            for (i in 0 until array.length()) {
                list.add(array.optString(i))
            }
        } catch (e: Exception) {
            // fallback
        }
        return list
    }

    @TypeConverter
    fun fromContradictionList(value: List<ContradictionItem>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach { item ->
            val obj = JSONObject().apply {
                put("statement1", item.statement1)
                put("statement2", item.statement2)
                put("explanation", item.explanation)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toContradictionList(value: String?): List<ContradictionItem> {
        if (value.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<ContradictionItem>()
        try {
            val array = JSONArray(value)
            for (i in 0 until array.length()) {
                val obj = array.optJSONObject(i)
                if (obj != null) {
                    list.add(
                        ContradictionItem(
                            statement1 = obj.optString("statement1", ""),
                            statement2 = obj.optString("statement2", ""),
                            explanation = obj.optString("explanation", "")
                        )
                    )
                }
            }
        } catch (e: Exception) {
            // fallback
        }
        return list
    }
}
