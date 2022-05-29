<template>
  <div>
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/courses/'+courseId }">{{name}} {{courseId}}</el-breadcrumb-item>
        <el-breadcrumb-item>项目</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <div style="height: 20px"></div>
    <el-row>
      <el-card style="margin: 18px 2%;width: 95%">
        <div slot="header" class="clearfix">
          <span><strong>当前项目</strong></span>
          <el-button v-if="$store.state.user.role.id===3" type="success" style="float: right;" @click="publish()">发布项目</el-button>
        </div>
        <el-table
          :data="now"
          stripe
          :default-sort = "{prop: 'endTime', order: 'descending'}"
          style="width: 100%"
          :max-height="tableHeight">
          <el-table-column
            prop="id"
            label="项目编号"
            width="100">
          </el-table-column>
          <el-table-column
            prop="projectName"
            label="项目名称"
            fit>
          </el-table-column>
          <el-table-column
            prop="endTime"
            label="截止时间"
            sortable
            fit>
          </el-table-column>
          <el-table-column
            prop="projectProportion"
            label="成绩百分比"
            fit>
          </el-table-column>
          <el-table-column
            label="操作"
            width="120">
            <template slot-scope="scope">
              <el-button
                @click="enter(scope.row)"
                type="text">
                查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <!--      <div style="margin: 20px 0 20px 0;float: left">-->
        <!--        <el-button>取消选择</el-button>-->
        <!--        <el-button>批量删除</el-button>-->
        <!--      </div>-->
      </el-card>
    </el-row>
    <el-row>
      <el-card style="margin: 18px 2%;width: 95%">
        <div slot="header" class="clearfix">
          <span><strong>历史项目</strong></span>
        </div>
        <el-table
          :data="past"
          stripe
          :default-sort = "{prop: 'endTime', order: 'descending'}"
          style="width: 100%"
          :max-height="tableHeight">
          <el-table-column
            prop="id"
            label="项目编号"
            width="100">
          </el-table-column>
          <el-table-column
            prop="projectName"
            label="项目名称"
            fit>
          </el-table-column>
          <el-table-column
            prop="endTime"
            label="截止时间"
            sortable
            fit>
          </el-table-column>
          <el-table-column
            prop="projectProportion"
            label="成绩百分比"
            fit>
          </el-table-column>
          <!--身份为教师时没有成绩列-->
<!--          <el-table-column-->
<!--            v-if="this.$store.state.user.role.id===5"-->
<!--            prop="score"-->
<!--            label="成绩"-->
<!--            fit>-->
<!--          </el-table-column>-->
          <el-table-column
            label="操作"
            width="120">
            <template slot-scope="scope">
              <el-button
                @click="enter(scope.row)"
                type="text">
                查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>
<!--        <div style="margin: 20px 0 20px 0;float: contour">-->
<!--          <span>总成绩:  {{23.55}}</span>-->
<!--        </div>-->
      </el-card>
    </el-row>

    <el-dialog
      title="项目设置"
      :visible.sync="dialogFormVisible"
      width="560px">
      <el-form :label-position="labelPosition" ref="loginFormRef" :model="loginForm" :rules="loginFormRules" label-width="100px" class="login_form" >
        <el-form-item  prop="projectName" label="项目名称">
          <el-input v-model="loginForm.projectName" ></el-input>
        </el-form-item>
        <el-form-item  prop="projectRequire" label="项目说明">
          <el-input v-model="loginForm.projectRequire" type="textarea" prefix-icon="el-icon-unlock"></el-input>
        </el-form-item>
        <el-form-item prop="startTime" label="项目开始时间">
          <el-date-picker
            v-model="loginForm.startTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            align="right">
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="endTime" label="项目截止时间">
          <el-date-picker
            v-model="loginForm.endTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            align="right"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="项目类型">
          <el-select v-model="loginForm.type" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="projectProportion" label="成绩百分比">
          <el-slider
          show-input
          :max="max"
          v-model="loginForm.projectProportion">
          </el-slider>
        </el-form-item>

      </el-form>
      <!--      <date-picker></date-picker>-->
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="create">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'CourseProjects',
  data () {
    return {
      name: '软件工程',
      courseId: '42034203',
      past: [],
      now: [],
      max: 0,

      // 表单的数据绑定对象
      dialogFormVisible: false,
      labelPosition: 'right',
      loginForm: {
        projectName:'',
        projectRequire:'',
        startTime:'',
        endTime:'',
        projectProportion:0,
        type: 0
      },
      // 这是表单的验证规则对象
      loginFormRules: {
        projectName: [
          { required: true, message: '请输入项目名称', trigger: 'blur' },
        ],
      },
      pickerOptions: {
        disabledDate(date){
          return date.getTime() < new Date();
        },
      },
      options: [{
        value: 0,
        label: '普通实验'
      }, {
        value: 1,
        label: '对抗练习'
      },{
        value: 2,
        label: '大型实验'
      }],
    }
  },
  computed: {
    tableHeight () {
      return window.innerHeight - 320
    }
  },
  created() {
    this.name = this.$store.state.course.courseName
    this.courseId = this.$route.params.id
    this.getPast()
    this.getNow()
  },
  methods: {
    getPast () {
      let _this = this
      this.$axios.get('/projects/'+this.$route.params.id+'/past').then(resp => {
        if (resp && resp.data.code === 200) {
          console.log('历史项目：', resp.data.object)
          _this.past = resp.data.object
        }
      })
    },
    getNow(){
      let _this = this
      this.$axios.get('/projects/'+this.$route.params.id+'/now').then(resp => {
        if (resp && resp.data.code === 200) {
          console.log('当前项目：', resp.data.object)
          _this.now = resp.data.object
        }
      })
    },
    enter(project){
      let _this = this
      if(project.type===1){
        _this.$router.push('/courses/'+_this.$route.params.id+'/contest/'+project.id)
      }else{
        _this.$router.push('/courses/'+_this.$route.params.id+'/project/'+project.id)
      }
    },
    publish(){
      let _this= this
      this.loginForm.startTime = new Date()
      this.$axios.get('/projects/'+this.$route.params.id+'/remain').then(resp => {
        if (resp && resp.data.code === 200) {
          // console.log('剩余可分配比例：', 100-resp.data.object*100, typeof resp.data.object)
          _this.max = 100-resp.data.object
        }
      })
      this.dialogFormVisible=true
    },
    create(){
      let _this= this
      // console.log(this.loginForm)
      this.$axios.post('/projects/publish',{
        projectName: this.loginForm.projectName,
        projectRequire:this.loginForm.projectRequire,
        projectProportion: this.loginForm.projectProportion,
        start: this.loginForm.startTime,
        endTime: this.loginForm.endTime,
        type: this.loginForm.type,
        course: {
          id: this.$route.params.id
        },
        publisher: this.$store.state.user.name
      }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$alert("项目发布成功！")
          this.dialogFormVisible=false
          this.getNow()
        }
      })

    }
  }
}
</script>

<style scoped>

</style>
