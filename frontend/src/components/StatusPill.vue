<!-- Copyright 2026 上海如静知华信息科技有限公司 -->
<script setup>defineProps({text:String})</script><template><span class="status" :class="{'is-red':['报警','不合格','暂停','待判定'].includes(text),'is-green':['运行','合格','已完工','已完成'].includes(text),'is-blue':['生产中','已下达','待检'].includes(text)}"><i></i>{{text}}</span></template>
