<template>
  <div>
    <el-card style="margin: 18px 2%;width: 95%">
      <el-row :gutter="20">
        <el-col :span="15">
          <create-course @onSubmit="getCourses()"></create-course>
        </el-col>
        <el-col :span="8">
          <el-input placeholder="请输入内容" >
            <el-button slot="append" icon="el-icon-search" ></el-button>
          </el-input>
        </el-col>
        <!-- <el-col :span="4">
          <el-button type="primary" @click="addOrderVisible = true">添加订单</el-button>
        </el-col> -->
      </el-row>
      <br/>

      <el-table :data="courses" stripe :min-height="tableHeight">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          type="index"
          label="序号"
          sortable
          width="100">
        </el-table-column>
        <el-table-column label="课程编号" prop="id" fit></el-table-column>
        <el-table-column label="课程名称" prop="courseName" fit></el-table-column>
        <el-table-column label="责任教师" prop="teacherName" fit></el-table-column>
        <el-table-column
          label="操作"
          width="120">
          <template slot-scope="scope">
            <el-button
              @click="editUser(scope.row)"
              type="text"
              size="small">
              编辑
            </el-button>
            <el-button
              @click="deleteUser(scope.row)"
              type="text"
              size="small">
              关闭
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <span class="divcss5">总条数: {{total}}</span>
    </el-card>
  </div>
</template>

<script>
import CreateCourse from "@/components/admin/CreateCourse";
export default {
  name: "CourseManage",
  components: {CreateCourse},
  data () {
    return {
      courses: [],
      total: 0,

      dialogFormVisible: false,
      selectedUser: [],
      selectedRolesIds: []
    }
  },
  computed: {
    tableHeight () {
      return window.innerHeight - 320
    }
  },
  created() {
    this.getCourses()
  },
  methods: {
    getCourses () {
      let _this = this
      this.$axios.get('/courses/all').then(resp => {
        if (resp && resp.data.code === 200) {
          _this.courses = resp.data.object
          _this.total = _this.courses.length
          console.log(_this.courses)
        }
      })
    },
    editUser (user) {
      this.dialogFormVisible = true
      this.selectedUser = user
      this.$store.commit('enterCourse',user)
      this.$router.push('/adminindex/course/'+user.id)
      // const roleIds = []
      // for (let i = 0; i < user.roles.length; i++) {
      //   roleIds.push(user.roles[i].id)
      // }
      // this.selectedRolesIds = roleIds
    },
    deleteUser(user){
      const _this = this
      this.$confirm('确定要关闭该课程吗？此操作不可逆','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.$axios.post('/courses/deletecourse?courseId='+user.id).then(resp => {
          if (resp && resp.data.code === 200) {
            this.$alert('课程 [' + user.courseName + '] 已关闭！')
            // 修改角色后重新请求用户信息，实现视图更新
            this.getCourses()
          } else {
            this.$alert(resp.data.msg)
          }
        })
      })
    },
  }
}
</script>

<style scoped>

</style>
