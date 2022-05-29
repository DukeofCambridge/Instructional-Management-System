<template>
  <el-container id="courses-body">
    <el-header style="margin-left: -20px">
      <Navbar />
    </el-header>
    <!--<el-row style="height: 100%;">-->
    <el-container>
      <el-aside style="height: 112%;width: 250px;margin-top: -8px;">
        <teacenter />
      </el-aside>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Navbar from '@/components/common/Navbar'
import Teacenter from '@/components/teacenter/Teacenter'
export default {
  name: 'TeacenterIndex',
  components: { Teacenter, Navbar }
}
</script>

<style scoped>
#courses-body {
  height: 95vh;
}
</style>
