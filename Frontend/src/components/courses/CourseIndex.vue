<template>
  <el-container id="courses-body">
    <el-header style="margin-left: -20px">
      <Navbar />
    </el-header>
    <!--<el-row style="height: 100%;">-->
    <el-container>
      <el-aside style="min-height: 800px;width: 250px;margin-top: -8px;">
        <course-menu />
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Navbar from '@/components/common/Navbar'
import CourseMenu from '@/components/courses/CourseMenu'
export default {
  name: 'CourseIndex',
  components: { CourseMenu, Navbar }
}
</script>

<style scoped>
#courses-body {
  /*height: 95vh;*/
}
</style>
