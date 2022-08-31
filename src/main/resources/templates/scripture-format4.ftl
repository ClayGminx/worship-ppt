<#-- 奇数行以“主领：”开头，偶数行以“会众：”开头，最后一行以“合　：”开头 -->
<#list scriptureVerseList as item>
<#if item?has_next == false>合　：<#elseif item?item_parity == "odd">主领：<#else>会众：</#if>${item.scripture}
</#list>