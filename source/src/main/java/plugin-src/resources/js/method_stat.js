
MethodStat = {
	            init : function(){
					var ds =new Ext.data.JsonStore({
							url: 'stat/datas',
							root: 'stat',
							totalProperty: "totalCount",
    						fields:  [
    							{name: 'method',mapping: 'key'},
    							{name: 'total',mapping: 'frequency'},
								{name: 'createTime',mapping: 'createTime'},
								{name: 'last', mapping: 'last'},
								{name: 'min', mapping: 'min'},
								{name: 'max', mapping: 'max'},
								{name: 'average', mapping: 'average'}
							],
							remoteSort: true
						});

						//ColumnModels
						var colModel =  new Ext.grid.ColumnModel([{
						   header: "����",
						   dataIndex: 'method',
						   width: 300
						},{
						   header: "��ʼʱ��",
						   dataIndex: 'createTime',
						   width: 115 
						},{
						   header: "���һ�ε���ʱ��",
						   dataIndex: 'last',
						   width: 115 
						},{
						   header: "��С��ʱ(ms)",
						   dataIndex: 'min',
						   sortable: true,
						   width: 80
						},{
						   header: "����ʱ(ms)",
						   dataIndex: 'max',
						   sortable: true,
						   width: 80
						},{
						   header: "ƽ����ʱ(ms)",
						   dataIndex: 'average',
						   sortable: true,
						   width: 80
						},{
						   header: "���ô���",
						   dataIndex: 'total',
						   sortable: true,
						   width: 80
						}
						
						]);

						var grid = new Ext.grid.Grid('grid', {
							ds: ds,
							cm: colModel,
							loadMask: true
						});
						
						var layout=new InfoGridLayout({
										grid : grid,
										paging : true,
										pageSize: 25,
										displayMsg:'��ʾ{0} - {1}������{2}����¼',
										emptyMsg: "��������"
									});
						layout.render();
						
	           }
	};

Ext.onReady(MethodStat.init, MethodStat, true);


