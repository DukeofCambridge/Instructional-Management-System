<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/courses/'+courseId }">{{name}} {{courseId}}</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/courses/'+courseId+'/project/'+projectId}">项目</el-breadcrumb-item>
        <!--        <el-breadcrumb-item :to="{path:'/courses/'+courseId+'/report/'+projectId}">{{projectName}}</el-breadcrumb-item>-->
        <el-breadcrumb-item>在线编辑</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <div style="height: 20px"></div>
<!--    <el-row>-->
<!--      <el-button style="margin: 10px" type="success" @click="save">提交</el-button>-->
<!--    </el-row>-->
    <el-row style="height:100vh ;">
      <mavon-editor
        v-model="report.contentMd"
        style="height: 100%;"
        ref=md
        @save="save"
        fontSize="16px">
        <!--        <button type="button" class="op-icon el-icon-document" :title="'摘要/封面'" slot="left-toolbar-after"-->
        <!--                @click="dialogVisible = true"></button>-->
      </mavon-editor>
    </el-row>
  </div>
</template>

<script>
import ImgUpload from "@/components/courses/ImgUpload";
export default {
  name: "ReportEditor",
  // components: {ImgUpload},
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
    this.load()
  },
  methods: {
    save(value, render) {
      // value 是 md，render 是 html
      this.$confirm('是否保存实验报告', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // console.log(value)
          this.$axios
            .post('/projects/save', {
              // title: this.report.title,
              contentMd: value,
              contentHtml: render,
              user:{
                id: this.$store.state.user.id
              },
              project:{
                id: this.projectId
              }
            }).then(resp => {
            if (resp && resp.data.code === 200) {
              this.$message({
                type: 'info',
                message: '成功'
              })
            }
          })
        }
      ).catch(() => {
        this.$message({
          type: 'info',
          message: '取消保存'
        })
      })
    },
    uploadImg() {
      this.article.articleCover = this.$refs.imgUpload.url
    },
    load() {
      // this.$axios
      //   .get('/projects/getReport?projectId=' + this.projectId + '&userId=' + this.$store.state.user.id).then(resp => {
      //   if (resp && resp.data.code === 200) {
      //     this.report = resp.data.object
      //   }else{
          this.$axios.get('/projects/template').then(resp=>{
            this.report = resp.data.object
          })
        // }
      // })
    }
  }
}
</script>

<style scoped>

</style>
