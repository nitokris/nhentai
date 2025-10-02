<script setup lang="ts">

import {ref} from "vue";

const step = ref(1)
const done1 = ref(false)
const done2 = ref(false)
const done3 = ref(false)

function reset() {
  done1.value = false
  done2.value = false
  done3.value = false
  step.value = 1
}

interface DataSourceType {
  label: string
  value: string
}

const chooseDatasource = ref<DataSourceType>(null)

const dataSourceType: DataSourceType[] = [
  {
    label: '哔咔漫画',
    value: 'PICA'
  },
  {
    label: 'DLSite',
    value: 'DLSITE'
  },
  {
    label: 'Fanza',
    value: 'FANZA'
  }
]

const comicId = ref('')

</script>

<template>

  <q-page padding>
    <div class="row">
      <div class="col-10 offset-1">
        <q-card>
          <q-item>
            <q-btn flat round icon="close"/>
          </q-item>
          <q-separator></q-separator>
          <div class="q-pa-md">
            <q-btn label="Reset" push color="white" text-color="primary" @click="reset" class="q-mb-md"/>

            <q-stepper
              v-model="step"
              header-nav
              ref="stepper"
              color="primary"
              animated
              flat
            >
              <q-step
                :name="1"
                title="选择数据源"
                icon="settings"
                :done="done1"
              >
                <q-form>
                  <div style="max-width: 300px">
                    <q-select name="dataSourceType" :options="dataSourceType" v-model="chooseDatasource" filled
                              clearable label="数据源类型"/>
                    <q-input v-model="comicId" name="comicId" label="唯一ID" type="text" filled
                             style="margin-top: 5px"/>
                  </div>
                </q-form>


                <q-stepper-navigation>
                  <q-btn @click="() => { done1 = true; step = 2 }" color="primary" label="Continue"/>
                </q-stepper-navigation>
              </q-step>

              <q-step
                :name="2"
                title="数据校准"
                caption="Optional"
                icon="create_new_folder"
                :done="done2"
              >

                <q-form>

                </q-form>

                <q-stepper-navigation>
                  <q-btn @click="() => { done2 = true; step = 3 }" color="primary" label="Continue"/>
                  <q-btn flat @click="step = 1" color="primary" label="Back" class="q-ml-sm"/>
                </q-stepper-navigation>
              </q-step>

              <q-step
                :name="3"
                title="结束"
                icon="add_comment"
                :done="done3"
              >
                Try out different ad text to see what brings in the most customers, and learn how to
                enhance your ads using features like ad extensions. If you run into any problems with
                your ads, find out how to tell if they're running and how to resolve approval issues.

                <q-stepper-navigation>
                  <q-btn color="primary" @click="done3 = true" label="Finish"/>
                </q-stepper-navigation>
              </q-step>
            </q-stepper>
          </div>
        </q-card>
      </div>
    </div>
  </q-page>

</template>

<style scoped lang="sass">

</style>
