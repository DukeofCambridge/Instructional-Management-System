<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/courses/'+courseId }">{{name}} {{courseId}}</el-breadcrumb-item>
        <el-breadcrumb-item>人员</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>

    <!-- 卡片视图区域 -->
    <div style="height: 20px"></div>
    <el-card style="margin: 18px 2%;width: 95%">
      <el-row v-if="this.$store.state.user.role.id===1">
        <el-col :span="6">
          <el-autocomplete
            v-model="state"
            :fetch-suggestions="querySearch"
            placeholder="请输入要导入的人员学号/工号"
            :trigger-on-focus="false"
            @select="handleSelect"
            size="small">
          </el-autocomplete>
        </el-col>

        <el-button type="primary" @click="importmem()"> 导入人员 </el-button>
        <!--        <el-col :span="8">-->
        <!--          <el-input placeholder="请输入内容"  clearable>-->
        <!--            <el-button slot="append" icon="el-icon-search"  ></el-button>-->
        <!--          </el-input>-->
        <!--        </el-col>-->
        <!--         <el-col :span="4">-->
        <!--          <el-select v-model="value" clearable placeholder="请选择">-->
        <!--    <el-option-->
        <!--      v-for="item in options"-->
        <!--      :key="item.value"-->
        <!--      :label="item.label"-->
        <!--      :value="item.value">-->
        <!--    </el-option>-->
        <!--  </el-select>-->
        <!--        </el-col>-->
      </el-row>
      <br/>
      <el-table :data="members" stripe  :default-sort = "{prop: 'role.name', order: 'descending'}" >
        <el-table-column type="index" label="序号" sortable width="100"></el-table-column>
        <el-table-column label="学号/工号" prop="username" fit></el-table-column>
        <el-table-column label="姓名" prop="name" fit></el-table-column>
        <!--        <el-table-column label="课程" prop="client_name"></el-table-column>-->
        <el-table-column label="身份" prop="role.name" sortable fit></el-table-column>
        <!--只有管理员才有操作栏-->
        <el-table-column
          label="操作"
          width="180"
          v-if="this.$store.state.user.role.id===1">
          <template slot-scope="scope">
            <el-button
              @click="deleteUser(scope.row)"
              type="text"
              size="small">
              移除
            </el-button>
            <el-button
              v-if="scope.row.role.id===3"
              @click="setmain(scope.row)"
              type="text"
              size="small">
              设为责任教师
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <span class="divcss5">总条数:{{total}}</span>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'CourseMembers',
  data () {
    return {
      name: '',
      courseId: '',
      members: [],
      total: 0,
      loading: true,
      result: [],
      select: 0,
      state: ''
    }
  },
  created() {
    this.name = this.$store.state.course.courseName
    this.courseId = this.$route.params.id
    this.getCourseInfo()
    console.log(this.$store.state.course)
  },
  methods: {
    getCourseInfo() {
      let _this = this
      // console.log(_this.$store.state.coursename)
      // console.log(this.$route.params.id)
      this.$axios.get('/courses/' + this.$route.params.id + '/members').then(resp => {
        if (resp && resp.data.code === 200) {
          // _this.courses = resp.data.object
          // console.log('课程人员信息：', resp.data.object)
          _this.members = resp.data.object
          _this.total = resp.data.object.length
          for (let i = 0; i < _this.members.length; i++) {
            if (_this.members[i].name === this.$store.state.course.teacherName) {
              _this.members[i].role.id = 2
              _this.members[i].role.name = "责任教师"
              break
            }
          }
        }
      })
    },
    querySearch(queryString, cb) {
      let _this = this
      if (queryString !== "") {
        // 用户输入内容后做模糊查询
        setTimeout(() => {
          let suggestion = []
          this.$axios.get('/admin/user/findcontaining?type=' + this.select + '&keyword=' + queryString).then(res => {
            if (res && res.data.code === 200) {
              let callBack = res.data.object
              // console.log('查询信息：', callBack)
              // 只有value键能被读取为匹配内容
              if (_this.select === 0) {
                suggestion = callBack.map((item) => {
                  return {
                    value: item['username']
                  }
                })
              } else if (_this.select === 1) {
                suggestion = callBack.map((item) => {
                  return {
                    value: item['name']
                  }
                })
              }
              // console.log("响应转换结果：", suggestion)
              if (suggestion.length === 0) {
                cb([{value: "暂无数据"}]);
              }
              // 查到数据
              else {
                cb(suggestion);
              }
            }
          })
        }, 1000);
      }
    },
    // 点击谁，就把谁放进去
    handleSelect(item) {
      this.result.push(item.value)
    },
    importmem() {
      let _this = this
      if (this.result !== []) {
        let _this = this
        console.log(_this.result)
        let usernameList = _this.result
        this.$axios.post('/courses/' + this.$route.params.id + '/import', usernameList).then(resp => {
          if (resp && resp.data.code === 200) {
            this.$alert("导入成功！")
            _this.getCourseInfo()
            // _this.courses = resp.data.object
            // console.log('课程人员信息：', resp.data.object)
          }
        })
      } else {
        this.$alert("请先查询并选择要导入的人员！")
      }
    },
    deleteUser(user) {
      let _this = this
      if (user.role.name !== "责任教师") {
        let usernameList = []
        usernameList.push(user.username)
        // console.log(usernameList)
        this.$axios.post('/courses/' + this.$route.params.id + '/remove', usernameList).then(resp => {
          if (resp && resp.data.code === 200) {
            this.$alert("移除成功！")
            _this.getCourseInfo()
            // _this.courses = resp.data.object
            // console.log('课程人员信息：', resp.data.object)
          }
        })
      } else {
        this.$alert("不能移除责任教师！")
      }
    },
    setmain(teacher) {
      this.$axios.post('/courses/' + this.$route.params.id + '/setmain?username=' + teacher.username).then(resp => {
        // console.log(teacher.username)
        if (resp && resp.data.code === 200) {
          this.$message("设置成功")
          this.$axios.get('/courses/' + this.$route.params.id + '/description').then(resp => {
            if (resp && resp.data.code === 200) {
              let info = resp.data.object
              this.$store.commit('enterCourse', info)
              this.getCourseInfo()
            }
            // _this.courses = resp.data.object
            // console.log('课程人员信息：', resp.data.object)
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.divcss5{
  font-size: 20px;
  color:#ff9955;
}
</style>
