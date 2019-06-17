<template>
  <div>
    {{item.default_value}}
  </div>
</template>

<script>
  export default {
    name: 'TipSpanContent',
    props: ['item', "index"]
  }
</script>
