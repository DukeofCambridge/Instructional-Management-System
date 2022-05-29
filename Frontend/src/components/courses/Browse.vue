<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/courses/'+courseId }">{{name}} {{courseId}}</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/courses/'+courseId+'/project/'+projectId}">项目</el-breadcrumb-item>
        <!--        <el-breadcrumb-item :to="{path:'/courses/'+courseId+'/report/'+projectId}">{{projectName}}</el-breadcrumb-item>-->
        <el-breadcrumb-item>学生报告</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <div style="height: 20px"></div>
    <!--    <el-row>-->
    <!--      <el-button style="margin: 10px" type="success" @click="save">提交</el-button>-->
    <!--    </el-row>-->
    <el-card style="text-align: left;width: 990px;margin: 35px auto 0 auto">
      <div>
        <span style="font-size: 20px"><strong>{{username}}的实验报告</strong></span>
        <el-divider content-position="center">提交时间: {{report.date}}</el-divider>
        <div class="markdown-body">
          <div v-html="report.contentHtml"></div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Browse",
  data () {
    return {
      report: {},
      dialogVisible: false,
      name:'',
      courseId:'',
      projectId:'',
    }
  },
  created() {
    this.name = this.$store.state.course.courseName
    this.courseId = this.$route.params.id
    this.projectId = this.$route.params.projectId
    this.username = this.$route.params.username
    this.load()
  },
  methods:{
    load() {
      this.$axios
        .get('/projects/getReport?projectId=' + this.projectId + '&username=' + this.username).then(resp => {
        if (resp && resp.data.code === 200) {
          this.report = resp.data.object
        } else {
          this.$alert("学生尚未提交实验报告！")
        }
      })
      // if(this.report===null){
      //   this.$alert("学生尚未提交实验报告！")
      // }
    }
  }
}
</script>

<style scoped>

</style>
