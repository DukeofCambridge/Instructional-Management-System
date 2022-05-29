<template>
  <div style="text-align: left">
    <el-button class="add-button" type="success" @click="dialogFormVisible = true">创建课程</el-button>
    <el-dialog
      title="创建课程"
      :visible.sync="dialogFormVisible"
      @close="clear"
      width="25%">
      <el-form ref="form" :model="loginForm" :rules="rules" label-position="left"
               label-width="0px">
        <el-form-item prop="id">
          <el-input type="text" v-model="loginForm.id"
                    auto-complete="off" placeholder="课程号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input type="text" v-model="loginForm.name"
                    auto-complete="off" placeholder="课程名"></el-input>
        </el-form-item>
        <el-form-item style="width: 100%">
          <el-button type="primary" style="width: 40%;margin-left: 30%" v-on:click="register">确认</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "CreateCourse",
  data () {
    return {
      dialogFormVisible: false,
      rules: {
        name: [
          { required: true, message: '请输入课程名', trigger: 'blur' },
        ],
      },
      loginForm: {
        id: '',
        name: '',
      },

    }
  },
  methods: {
    clear () {
      this.loginForm = {
        id: '',
        name: ''
      }
    },
    register () {
      let _this = this
      this.$refs.form.validate(async valid =>{
        if(!valid) return
        console.log(this.loginForm)
        this.$axios
          .post('/courses/create', {
            id: this.loginForm.id,
            name: this.loginForm.name
          })
          .then(resp => {
            if (resp.data.code === 200) {
              this.$alert('创建成功', '提示', {
                confirmButtonText: '确定'
              })
              this.clear()
              this.$emit('onSubmit')
              this.dialogFormVisible = false
            } else {
              this.$alert(resp.data.msg, '提示', {
                confirmButtonText: '确定'
              })
            }
          })
          .catch(failResponse => {})
      })

    }
  }
}
</script>

<style scoped>
/*.add-button {*/
/*  margin: 18px 0 0 10px;*/
/*}*/
</style>
