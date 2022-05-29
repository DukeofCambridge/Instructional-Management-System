<template>
  <el-menu
    :default-active="currentPath"
    class="el-menu"
    router
    mode="vertical"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
    :collapse="isCollapse">
    <div style="height: 20px;" />
    <!--index 没有用但是必需字段-->
<!--      <span slot="title" style="font-size: 17px;">-->
<!--        <i :class="el-icon-user"></i>-->
<!--         用户管理-->
<!--      </span>-->
      <el-menu-item index="/adminindex/user">
        <i class="el-icon-user"></i>
        用户管理
      </el-menu-item>
      <el-menu-item index="/adminindex/courses">
        <i class="el-icon-menu"></i>
        课程管理
      </el-menu-item>
      <el-menu-item index="/adminindex/role">
        <i class="el-icon-menu"></i>
        角色权限
      </el-menu-item>
      <el-menu-item index="/adminindex/board">
        <i class="el-icon-date"></i>
        通知公告
      </el-menu-item>
      <el-menu-item index="/adminindex/log">
        <i class="el-icon-notebook-2"></i>
        日志管理
      </el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: 'AdminMenu',
  data () {
    return {
      isCollapse: false
    }
  },
  computed: {
    currentPath () {
      return this.$route.path
    }
  }
}
</script>

<style scoped>
.el-menu {
  border-radius: 5px;
  height: 100%;
  text-align: center;
  font-size: 80px;
}
</style>
