<template>
  <el-menu
    default-active="0"
    class="el-menu"
    router
    mode="vertical"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
    :collapse="isCollapse">
    <el-menu-item index="0" :route="{path: '/courses/'+this.$route.params.id+'/home'}">
      主 页
    </el-menu-item>
    <el-menu-item index="1" :route="{path: '/courses/'+this.$route.params.id+'/projects'}">
      项 目
    </el-menu-item>
    <el-menu-item index="2" :route="{path: '/courses/'+this.$route.params.id+'/members'}">
      人 员
    </el-menu-item>
    <el-menu-item index="3" :route="{path: '/courses/'+this.$route.params.id+'/files'}">
      文 件
    </el-menu-item>
    <el-menu-item index="4" :route="{path: '/courses/'+this.$route.params.id+'/grades'}">
      成 绩
    </el-menu-item>
    <el-menu-item index="5" :route="{path: '/courses/'+this.$route.params.id+'/discuss'}">
      讨 论
    </el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: 'CourseMenu',
  data () {
    return {
      isCollapse: false
    }
  },
}
</script>

<style scoped>
.el-menu {
  height: 100%;
  text-align: center;
  font-size: 80px;
}
</style>
