<template>
<div>
  <el-row style="margin: 18px 0px 0px 18px ">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/courses/'+courseId }">{{name}} {{courseId}}</el-breadcrumb-item>
      <el-breadcrumb-item>主页</el-breadcrumb-item>
    </el-breadcrumb>
  </el-row>
  <div style="height: 20px"></div>
  <el-row>
    <el-card style="margin: 18px 2%;width: 95%">
      <div slot="header" class="clearfix">
        <span><strong>课程介绍</strong></span>
      </div>
      <div class="text item">
        {{description}}
      </div>
    </el-card>
  </el-row>
  <el-row>
    <el-card style="margin: 18px 2%;width: 95%">
      <div slot="header" class="clearfix">
        <span><strong>历史通知</strong></span>
        <el-button type="primary" style="float: right" @click="dialogFormVisible2=true" v-if="$store.state.user.role.id===3">发布公告</el-button>
      </div>
      <el-table
        :data="board"
        stripe
        style="width: 100%"
        :default-sort = "{prop: 'date', order: 'descending'}"
        :max-height="tableHeight">
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
        <el-table-column
          prop="title"
          label="通知标题"
          fit>
        </el-table-column>
        <el-table-column
          prop="date"
          label="发布时间"
          sortable
          fit>
        </el-table-column>
        <el-table-column
          label="操作"
          width="120">
          <template slot-scope="scope">
            <el-button
              @click="editNotice(scope.row)"
              type="text"
              size="small">
              查看
            </el-button>
            <el-button
              v-if="$store.state.user.role.id===3"
              @click="del(scope.row)"
              type="text"
              size="small">
              移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin: 20px 0 20px 0;float: left">
        <el-button>取消选择</el-button>
        <el-button>批量删除</el-button>
      </div>
    </el-card>
  </el-row>

  <!--查看历史公告的弹窗-->
  <el-dialog
    :title= "selectedNotice.title"
    :visible.sync="dialogFormVisible"
    v-if="board.length">
    <el-form v-model="selectedNotice" style="text-align: left" ref="dataForm">
      <el-form-item label="详细内容" label-width="120px" prop="name">
        <label> {{selectedNotice.info}}</label>
      </el-form-item>
      <el-form-item label="发布人" label-width="120px" prop="username">
        <label>{{selectedNotice.publisher}}</label>
      </el-form-item>
      <el-form-item label="发布日期" label-width="120px" prop="phone">
        <label> {{selectedNotice.date}}</label>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
<!--      <el-button type="warning" @click="onSubmit(selectedNotice)">编 辑</el-button>-->
      <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
    </div>
  </el-dialog>

  <!--发布公告的弹窗-->
    <el-dialog
      title="发布公告"
      :visible.sync="dialogFormVisible2"
      @close="clear"
      width="25%"
      >
      <el-form ref="form" :model="loginForm" :rules="rules" label-position="left"
               label-width="0px">
        <el-form-item prop="title">
          <el-input type="text" v-model="loginForm.title"
                    auto-complete="off" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item prop="info">
          <el-input type="textarea" v-model="loginForm.info"
                    auto-complete="off" placeholder="详细内容"></el-input>
        </el-form-item>
        <el-form-item style="width: 100%">
          <el-button type="primary" style="width: 40%;margin-left: 30%" v-on:click="publish">确认</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

</div>
</template>

<script>
export default {
  name: 'CourseHome',
  data () {
    return {
      name: '',
      courseId: '',

      description: '',
      // 公告板信息
      board: [],
      dialogFormVisible: true,
      selectedNotice:[],
      // 发布公告

      dialogFormVisible2: false,
      rules: {
        title: [
          {required: true, message: '标题是必须的', trigger: 'blur'},
        ],
      },
      loginForm: {
        title: '无',
        info: '无',
      },


    }
  },
  created() {
    this.name = this.$store.state.course.courseName
    this.courseId = this.$route.params.id
    this.getBoard()
    this.getCourseInfo()
  },
  computed: {
    tableHeight () {
      return window.innerHeight
    }
  },
  methods: {
    editNotice (notice) {
      this.dialogFormVisible = true
      this.selectedNotice = notice
      this.selectedNotice.info = notice.info!==""? notice.info:"无"
    },
    getCourseInfo () {
      let _this = this
      this.$axios.get('/courses/'+this.$route.params.id+'/description').then(resp => {
        if (resp && resp.data.code === 200) {
          console.log('课程信息：', resp.data.object)
          let info = resp.data.object
          _this.$store.commit('enterCourse',info)
          // console.log(_this.$store.state.coursename, _this.$store.state.user.username)
          _this.name = info.courseName
          _this.courseId = info.id
          _this.description = info.courseDescription
        }
      })
    },
    getBoard(){
      let _this =this
      this.$axios.get('/courses/'+this.$route.params.id+'/board').then(resp => {
        if (resp && resp.data.code === 200) {
          // _this.courses = resp.data.object
          console.log('公告板信息：', resp.data.object)
          _this.board = resp.data.object
          _this.selectedNotice=resp.data.object[resp.data.object.length-1]
          _this.selectedNotice.info = _this.selectedNotice.info!==""? _this.selectedNotice.info:"无"
        }
      })
    },
    publish(){
      let _this= this
      // console.log(this.loginForm.title,this.loginForm.info,this.$store.state.user.name,this.$route.params.id)
      this.$axios.post('/courses/notice',{
        title: this.loginForm.title,
        info: this.loginForm.info,
        publisher: this.$store.state.user.name,
        course: {
          id: this.$route.params.id
        }
      }).then(resp => {
        if (resp && resp.data.code === 200) {
          // _this.courses = resp.data.object
          console.log('公告板信息：', resp.data.object)
          _this.dialogFormVisible2=false
          _this.getBoard()
        }
      })
    },
    clear () {
      this.loginForm = {
        title: '',
        info: ''
      }
    },
    del(notice){
      let _this=this
      this.$axios.post('/courses/deletenotice?boardId='+notice.id).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$alert("删除成功！")
          _this.getBoard()
        }
      })
    }
  }
}
</script>

<style scoped>
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}
</style>
