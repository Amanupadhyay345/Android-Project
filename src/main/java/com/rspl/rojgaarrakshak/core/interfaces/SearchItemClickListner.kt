package com.rspl.rojgaarrakshak.core.interfaces

interface SearchItemClickListner {

    fun onJobItemClicked(pos: Int,id:Int,jobtitle:String,companyName:String
                         ,salary:String,jobDescription:String,experience:String,
                         designation:String,location:String,expDate:String,skillTitle:String)
}