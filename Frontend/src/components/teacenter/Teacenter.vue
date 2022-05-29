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
    <el-menu-item index="/teacentermy">
      我 的 课 程
    </el-menu-item>
    <el-menu-item index="/teacenterall">
      全 校 课 程
    </el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: 'Teacenter',
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
  height: 100%;
  text-align: center;
  font-size: 80px;
}
</style>
