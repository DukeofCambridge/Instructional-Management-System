<template>
  <div class="block">
    <el-row style="margin: 18px 0px 0px 18px ">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/courses/'+courseId }">{{name}} {{courseId}}</el-breadcrumb-item>
        <el-breadcrumb-item>讨论</el-breadcrumb-item>
      </el-breadcrumb>
    </el-row>
    <div style="height: 20px"></div>

    <el-card style="margin: 18px 2%;width: 95%">
      <el-container direction="horizontal">
        <el-button type="success" @click="dialogFormVisible=true">添加反馈</el-button>
        <div style="margin-left:80px;margin-right:20px;border-radius: 20px;height: 20px;width: 20px;background-color: #409eff"></div>
        <span><strong>学习进展</strong></span>
        <div style="margin-left:40px;margin-right:20px;border-radius: 20px;height: 20px;width: 20px;background-color: #67C23A"></div>
        <span><strong>教学建议</strong></span>
        <div style="margin-left:40px;margin-right:20px;border-radius: 20px;height: 20px;width: 20px;background-color: #E6A23C"></div>
        <span><strong>困难疑惑</strong></span>
      </el-container>
      <div style="height: 20px"></div>
      <el-timeline v-for="item in com" :key="item.id" :reverse="reverse">
        <el-timeline-item :timestamp="item.time" placement="top" :type="item.type" size="large">
          <el-card>
            <h4>{{item.user.name}}</h4>
            <p>{{ item.description }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>
    <el-dialog
      title="反馈学习情况"
      :visible.sync="dialogFormVisible"
      width="560px">
      <el-form :label-position="labelPosition"  :rules="loginFormRules" label-width="100px" class="login_form" >
        <el-form-item  label="反馈内容">
          <el-input v-model="description" ></el-input>
        </el-form-item>
        <el-form-item label="反馈标签">
          <el-select v-model="type" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

      </el-form>
      <!--      <date-picker></date-picker>-->
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="feedback">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Comment",
  data(){
    return {
      name: '',
      courseId: '',
      com:[
        {
          title:'请教XP的知识',
          info: '略略略',
        }
      ],
      labelPosition: 'right',
      reverse: true,
      type: '',
      dialogFormVisible: false,
      description: '',
      options: [{
        value: 'primary',
        label: '学习进展'
      }, {
        value: 'success',
        label: '教学建议'
      },{
        value: 'warning',
        label: '困难疑惑'
      }],
      loginFormRules: {
        description: [
          { required: true, message: '内容不能为空', trigger: 'blur' },
        ],
      },
    }
  },
  created() {
    this.name = this.$store.state.course.courseName
    this.courseId = this.$route.params.id
    this.getall()
  },
  methods:{
    getall(){
      this.$axios.get('/courses/'+this.courseId+'/fboard').then(resp => {
        if (resp && resp.data.code === 200) {
          // console.log('反馈信息：', resp.data.object)
          this.com = resp.data.object
          this.com = this.com.reverse()
        }
      })
    },
    feedback(){
      let _this= this
      // console.log(this.type)
      this.$axios.post('/courses/feedback',{
        type: this.type,
        user:{
          id: this.$store.state.user.id
        },
        description: this.description,
        courseId: this.courseId
      }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$alert("反馈成功！")
          this.description = '';
          this.dialogFormVisible=false
          this.getall()
        }
      })

    }
  }
}
</script>

<style scoped>

</style>
