package com.rspl.rojgaarrakshak.core.interfaces

interface WorkHistoryListener {

    fun onItemclick(pos:Int,comoanyname:String,jobdescription:String,joblocation:String,dateofjoining:String
                    ,enddate:String,Salary:String,empname:String)
}