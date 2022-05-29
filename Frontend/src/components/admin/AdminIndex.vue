<template>
  <el-container id="admin-body">
    <el-header style="z-index: 1;height: 80px;margin-left: -20px">
      <Header />
    </el-header>
    <!--<el-row style="height: 100%;">-->
    <el-container>
      <el-aside style="height: 112%;width: 250px">
        <admin-menu />
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Header from '@/components/admin/Header'
import AdminMenu from '@/components/admin/AdminMenu'
export default {
  name: 'AdminIndex',
  components: { AdminMenu, Header }
}
</script>

<style scoped>
#admin-body {
  height: 95vh;
}
</style>
